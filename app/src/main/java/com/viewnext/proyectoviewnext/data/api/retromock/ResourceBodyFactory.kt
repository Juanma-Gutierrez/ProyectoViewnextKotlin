package com.viewnext.proyectoviewnext.data.api.retromock

import co.infinum.retromock.BodyFactory
import java.io.IOException
import java.io.InputStream

/**
 * Implementation of [BodyFactory] that creates an InputStream from a resource file.
 */
internal class ResourceBodyFactory : BodyFactory {

    /**
     * Creates an InputStream from a resource file.
     *
     * @param input The name of the resource file.
     * @return An InputStream representing the resource file.
     * @throws IOException If an I/O error occurs while creating the InputStream.
     */
    @Throws(IOException::class)
    override fun create(input: String): InputStream {
        return ResourceBodyFactory::class.java.classLoader!!.getResourceAsStream(input)
    }
}