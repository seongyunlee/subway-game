package site.kkrupp.subway.utill

import org.springframework.core.convert.converter.Converter

class StringToEnumConverter : Converter<String, GameType> {

    override fun convert(source: String): GameType {
        return GameType.valueOf(source)
    }

}