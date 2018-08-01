package com.ramensea.stringcheese2.process.implementations

import com.ramensea.stringcheese2.models.GeneralOutput
import com.ramensea.stringcheese2.models.LanguageTextValueSet
import com.ramensea.stringcheese2.models.Options
import com.ramensea.stringcheese2.models.Output
import com.ramensea.stringcheese2.process.interfaces.TextTranslationGenerator

class AndroidGenerator(): TextTranslationGenerator {
    override val supportsKeyClassGeneration: Boolean = false

    override fun translateKey(key: String): String {
        return key.lowercaseFirstCharacter().trim().replace("\\s+".toRegex(), " ").replace(" ","_").replace("_[A-Z]".toRegex(),{ matchResult ->
            matchResult.value.toLowerCase()
        }).replace("[A-Z]".toRegex(),{ matchResult ->
            "_" + matchResult.value.toLowerCase()
        })
    }
    fun translateKeyToClassMethod(key: String, capitolizeFirstLetter: Boolean = false): String {
        val s = translateKey(key).replace("_.".toRegex(),{ matchResult ->
            matchResult.value.substring(1).toUpperCase()
        })
        return if (capitolizeFirstLetter) s.uppercaseFirstCharacter() else s
    }
    override fun process(options: Options, languageSet: LanguageTextValueSet): Output {
        val builder = StringBuilder()
        builder.append("<!--String Cheese generated at:${options.timeStamp} -->\n" +
                "<resources>\n")
        for (tv in languageSet.textValues) {
            builder.append("    <string name=\"${translateKey(tv.key)}\"${if (!tv.isTranslatable) " translatable=\"false\"" else ""}>${tv.text}</string>")
        }

        builder.append("</resources>\n")
        return GeneralOutput(options.outputPlatform,languageSet.languageShort,builder.toString(),false)
    }

    override fun processKeyClass(options: Options, rootSet: LanguageTextValueSet): Output {
        throw Exception("AndroidGenerator does not support generating a key class.")
//        val options = options as AndroidOptions
//        val builder = StringBuilder()
//        builder.append("//String Cheese generated at:${options.timeStamp}\n" +
//                "class ${options.keyClassName}(context: Context? = null) {\n" +
//                "    private val context = ${if (options.weakReferenceContext) "WeakReference<Context>(context)" else "context"}\n\n")
//
//        for (textValue in rootSet.textValues) {
//            if (!textValue.hasArguments()) builder.append("    fun get${translateKeyToClassMethod(textValue.key,true)}(context: Context = null)")
//            else {
//
//            }
//        }
//
//        return GeneralOutput(options.outputPlatform,"",builder.toString(),true)
    }

    data class AndroidOptions(override val timeStamp: String,
                              override val outputPlatform: String,
                              override val rootLanguageId: String,

                              override val alertForMissingTexts: Boolean,
                              override val fillInMissingStrings: Boolean,
                              override val fillInMissingStringsInBlank: Boolean,

                              override val sortKeys: Boolean,

                              override val outputKeyClass: Boolean,
                              override val keyClassName: String,

                              //android options
                              val weakReferenceContext: Boolean): Options {
    }
}

fun String.lowercaseFirstCharacter(): String {
    if (this.length < 1) return this.toLowerCase()
    return this[0].toLowerCase() + this.removeRange(0,1)
}
fun String.uppercaseFirstCharacter(): String {
    if (this.length < 1) return this.toUpperCase()
    return this[0].toUpperCase() + this.removeRange(0,1)
}