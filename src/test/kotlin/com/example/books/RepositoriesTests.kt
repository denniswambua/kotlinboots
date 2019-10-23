package com.example.books

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val articleRepository: ArticleRepository){

    @Test
    fun `When findByIdOrNull then return Article`(){
        val denno = User("denno", "Dennis", "Wambz")
        entityManager.persist(denno)
        val article = Article("My Title", "My headline", "content...file", denno)
        entityManager.persist(article)
        entityManager.flush()
        val found = articleRepository.findByIdOrNull(article.id!!)
        assertThat(found).isEqualTo(article)
    }

    @Test
    fun `When findByLogin then return User`(){
        val denno = User("denno", "Dennis", "Wambz")
        entityManager.persist(denno)
        entityManager.flush()

        val user = userRepository.findByLogin(denno.login)
        assertThat(user).isEqualTo(denno)
    }
}