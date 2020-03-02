package com.romankagan.pyramid

import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.springframework.http.HttpStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PyramidControllerTestsRussian {
	@Autowired
	lateinit var testRestTemplate: TestRestTemplate

	@Test
	fun testWordIsPyramid() {
		val word="банана"
		val result = testRestTemplate.getForEntity("/api/v1/pyramid?word=$word", String::class.java)
		Assert.assertThat(result.body, CoreMatchers.`is`("Success. The word:$word is a `pyramid word`."))
		Assert.assertThat(result.statusCode, CoreMatchers.`is`(HttpStatus.OK))
	}

	@Test
	fun testWordIsBlank() {
		val word="бан ана"
		val result = testRestTemplate.getForEntity("/api/v1/pyramid?word=$word", String::class.java)
		Assert.assertThat(result.body, CoreMatchers.`is`("Failed. The word:$word has blank character(s)."))
		Assert.assertThat(result.statusCode, CoreMatchers.`is`(HttpStatus.NOT_FOUND))
	}

	@Test
	fun testWordHasDups() {
		val word="лаббиринт"
		val result = testRestTemplate.getForEntity("/api/v1/pyramid?word=$word", String::class.java)
		Assert.assertThat(result.body, CoreMatchers.`is`("Failed. The word:$word has duplicates."))
		Assert.assertThat(result.statusCode, CoreMatchers.`is`(HttpStatus.NOT_FOUND))
	}

	@Test
	fun testWordNotPyramid() {
		val word="латиница"
		val result = testRestTemplate.getForEntity("/api/v1/pyramid?word=$word", String::class.java)
		Assert.assertThat(result.body, CoreMatchers.`is`("Failed. The word:$word is not a `pyramid word`."))
		Assert.assertThat(result.statusCode, CoreMatchers.`is`(HttpStatus.NOT_FOUND))
	}

	@Test
	fun testNotPyramidSingleCharAbsent() {
		val word="wawaw"
		val result = testRestTemplate.getForEntity("/api/v1/pyramid?word=$word", String::class.java)
		Assert.assertThat(result.body, CoreMatchers.`is`("Failed. The word:$word is not a `pyramid word`."))
		Assert.assertThat(result.statusCode, CoreMatchers.`is`(HttpStatus.NOT_FOUND))
	}
}
