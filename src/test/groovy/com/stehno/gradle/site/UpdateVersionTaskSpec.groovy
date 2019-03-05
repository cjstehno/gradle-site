/*
 * Copyright (C) 2019 Christopher J. Stehno <chris@stehno.com>
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

import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class UpdateVersionTaskSpec extends Specification implements UsesGradleBuild {

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()

    final String buildTemplate = '''
        plugins {
            id 'com.stehno.gradle.site'
        }
        
        version = '1.2.3'
        
        repositories {
            jcenter()
        }
        ${config.extension ?: ''}
    '''

    def 'updateVersion without site (updates)'() {
        setup:
        buildFile(extension: '')

        fileTree().file('README.md', 'This is for version 1.0.3')

        when: 'the updateVersion task is run'
        BuildResult result = gradleRunner('updateVersion -Pfrom=1.0.3').build()

        then: 'the build is successful'
        totalSuccess result

        and: 'the version is updated'
        fileExists('README.md', 'This is for version 1.2.3')
    }

    def 'updateVersion with extra files (updates)'() {
        setup:
        buildFile(extension: '''
            site {
                versionedFile 'foo.txt'
            }
        ''')

        def tree = fileTree()
        tree.file('README.md', 'This is for version 1.0.3')
        tree.file('foo.txt', 'This is another version 1.0.3')

        when: 'the updateVersion task is run'
        BuildResult result = gradleRunner('updateVersion -Pfrom=1.0.3').build()

        then: 'the build is successful'
        totalSuccess result

        and: 'the version is updated'
        fileExists('README.md', 'This is for version 1.2.3')
        fileExists('foo.txt', 'This is another version 1.2.3')
    }

    def 'updateVersion with site (updates)'() {
        setup:
        buildFile(extension: '')

        FileTreeBuilder tree = fileTree()
        tree.file('README.md', 'This is for version 1.0.3')
        tree.dir('src/site') {
            file('index.html', 'Site for version 1.0.3')
        }

        when: 'the updateVersion task is run'
        BuildResult result = gradleRunner('updateVersion -Pfrom=1.0.3').build()

        then: 'the build is successful'
        totalSuccess result

        and: 'the version is updated'
        fileExists('README.md', 'This is for version 1.2.3')
        fileExists('src/site/index.html', 'Site for version 1.2.3')
    }

}
