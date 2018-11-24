package com.ramensea.stringcheese2.process.implementations.generators


open class StringSanitizer {
    open fun sanitizeKey(key: String): String {
        //only allow A-Za-Z0-9 spaces and _ in a key
        return key.replace("([^A-Za-z0-9 _])".toRegex(),"")
    }
    open fun sanitizeText(text: String): String {
        return text.regexEscape('"').regexEscape('\'')
    }
    open fun sanitizeArgument(arg: String): String {
        return arg
    }
    open fun sanitizeArgumentString(args: String): String {
        return args.replace("([^A-Za-z0-9_,])".toRegex(),"")
    }
}




fun String.regexEscape(char: Char): String {
    return replace("(\\\\$char|$char)".toRegex()) { matchResult: MatchResult -> //escapes "
        if (matchResult.value == "$char") return@replace "\\$char"
        matchResult.value
    }
}