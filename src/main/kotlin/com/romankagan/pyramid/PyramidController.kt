package com.romankagan.pyramid


import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder
import java.util.*


@RestController
@Suppress("unused")
class PyramidController @Autowired constructor(
) {
    enum class STATUS { SUCCESS, BLANK, DUPLICATE, FAIL }
    val BLANK_CHARACTER = ' '

    private val logger = LoggerFactory.getLogger(PyramidController::class.java)

    @GetMapping("/api/v1/pyramid")
    @ResponseBody
    fun isPyramid(@RequestParam(value = "word") word: String):ResponseEntity<String> {
        val urlDecoded = URLDecoder.decode(word, "UTF-8")
        return when (detectPyramid(urlDecoded)) {
            STATUS.SUCCESS -> ResponseEntity("Success. The word:$urlDecoded is a `pyramid word`.", HttpStatus.OK)
            STATUS.BLANK -> ResponseEntity("Failed. The word:$urlDecoded has blank character(s).", HttpStatus.NOT_FOUND)
            STATUS.DUPLICATE -> ResponseEntity("Failed. The word:$urlDecoded has duplicates.", HttpStatus.NOT_FOUND)
            STATUS.FAIL -> ResponseEntity("Failed. The word:$urlDecoded is not a `pyramid word`.", HttpStatus.NOT_FOUND)
        }
    }

    @RequestMapping("/")
    fun home(): String? {
        return "Hello `Pyramid Word` challenge!"
    }

    fun detectPyramid(word:String):STATUS{
        val individualLetters: SortedMap<String, Int> = TreeMap()
        var previousLetter = ""
        for(singleChar in word){
            if (singleChar.equals(BLANK_CHARACTER)) return STATUS.BLANK // checking if blanks exist here
            val letter = singleChar.toString()
            if (previousLetter.equals(letter)) return STATUS.DUPLICATE //checking if dups exist here

            var value:Int  = individualLetters.getOrDefault(letter,0)
            individualLetters.put(letter, value+1)
            previousLetter = letter
        }
        val valuesSet: Set<Int> = TreeSet(individualLetters.values)
        //we're checking that the sizes of unique letters like b,,a (size=3) equals to (1,2,3) (size=3 also)
        if ( ! individualLetters.keys.size.equals(valuesSet.size))
            return STATUS.FAIL

        var counter = 1
        for (v in valuesSet){
            if ( ! v.equals(counter++))
                return STATUS.FAIL
        }
        return STATUS.SUCCESS
    }
}