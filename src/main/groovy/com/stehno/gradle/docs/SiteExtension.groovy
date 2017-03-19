/*
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
package com.stehno.gradle.docs

/**
 * Configuration for the plugin tasks.
 */
class SiteExtension {

    /**
     * The directory where the site source content resides. This defaults to "src/site".
     */
    String srcDir = 'src/site'

    /**
     * The directory where the built site content will be generated. This defaults to "build/site".
     */
    String buildDir = 'build/site'

    /**
     * The URL for the deployed documentation site, to be used in content verification.
     */
    String siteUrl

    /**
     * Additional paths to be tested against the deployed documentation site.
     */
    final List<String> testedPaths = []

    /**
     * Additional files which may contain documented version information to be managed.
     */
    final List<String> versionedFiles = []

    /**
     * Adds a file that may contain documented version information to be managed.
     *
     * @param file the file path string
     */
    void versionedFile(final String file) {
        versionedFiles << file
    }

    /**
     * Adds a path to be tested against the deployed documentation site.
     *
     * @param path the path to be tested
     */
    void testedPath(final String path) {
        testedPaths << path
    }
}
