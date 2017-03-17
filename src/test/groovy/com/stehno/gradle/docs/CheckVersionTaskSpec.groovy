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

import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class CheckVersionTaskSpec extends Specification implements UsesGradleBuild {

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()

    final String buildTemplate = '''
        plugins {
            id 'com.stehno.gradle.project-docs'
        }
        
        version = '1.2.3'
        
        repositories {
            jcenter()
        }
        ${config.extension ?: ''}
    '''

    def 'checkVersion (ok)'(){
        setup:
        buildFile(extension: '')

        fileTree().file('README.md', 'This is for version 1.2.3')

        when: 'the checkVersion task is run'
        BuildResult result = gradleRunner('checkVersion').build()

        then: 'the build is successful'
        totalSuccess result
    }

    def 'checkVersion (fail)'(){
        setup:
        buildFile(extension: '')

        fileTree().file('README.md', 'This is for version 0.1.0')

        when: 'the checkVersion task is run'
        BuildResult result = gradleRunner('checkVersion').build()

        then: 'the build fails'
        def ex = thrown(Exception)
        ex.message.contains('The documented project version does not match the project version: Run "./gradlew updateVersion -Pfrom=OLD_VERSION" and try again.')
    }
}
