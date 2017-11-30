/**
 * Copyright (C) 2017 Christopher J. Stehno <chris@stehno.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.gradle.site

/**
 * Configuration for the plugin tasks.
 */
open class SiteExtension {

    /**
     * The directory where the site source content resides. This defaults to "src/site".
     */
    var srcDir = "src/site"

    /**
     * The directory where the built site content will be generated. This defaults to "build/site".
     */
    var buildDir = "build/site"

    /**
     * The URL for the deployed documentation site, to be used in content verification.
     */
    lateinit var siteUrl: String

    val testedPaths = mutableListOf<String>()
    val versionedFiles = mutableListOf<String>()
    val variables = mutableMapOf<String, Any>()
    val assetDirs = mutableListOf<AssetDir>()

    /**
     * Adds a file that may contain documented version information to be managed.
     *
     * @param file the file path string
     */
    fun versionedFile(file: String) = versionedFiles.add(file)

    /**
     * Adds a path to be tested against the deployed documentation site.
     *
     * @param path the path to be tested
     */
    fun testedPath(path: String) = testedPaths.add(path)

    /**
     * Adds the specified variable replacement to the site template generation.
     *
     * @param name the variable name
     * @param value the variable value
     */
    fun variable(name: String, value: Any) {
        variables[name] = value
    }

    /**
     * Adds the specified variable replacements to the site template generation.
     *
     * @param vars the map of variable replacements
     */
    fun variables(vars: Map<String, Any>) = variables.putAll(vars)

    /**
     * Used to specify an additional asset directory to be copied (un-processed) into the site build directory.
     *
     * @param attrs additional asset information ("into", "external", and "replace" supported)
     * @param dir the added directory (under the site source dir) - ant patterns are allowed
     */
    fun assetDir(attrs: Map<String, Any> = mapOf(), dir: String) {
        assetDir(dir, attrs["into"].toString(), attrs["external"] as Boolean, attrs["replace"] as Boolean)
    }

    fun assetDir(dir: String, into: String?, external: Boolean = false, replace: Boolean = false) {
        assetDirs.add(AssetDir(dir, into, external, replace))
    }

    // FIXME: closure version
}

data class AssetDir(val dir: String, val into: String?, val external: Boolean, val replace: Boolean)