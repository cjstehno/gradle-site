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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * FIXME: document
 */
class CheckVersionTask extends DefaultTask {

    static final String NAME = 'checkVersion'

    @TaskAction @SuppressWarnings('GroovyUnusedDeclaration') void checkVersion() {
        logger.lifecycle "Verifying that the documentation references version ${project.version}..."

        // TODO: should be able to configure this
        List<String> checkedFiles = ['README.md']

        // Not the most efficient way to do this but should be ok for now
        boolean documented = checkedFiles.every { f ->
            boolean ok = project.file(f).text.contains(project.version)
            logger.info " - Checking: $f: $ok"
            ok
        }

        assert documented, 'The documented project version does not match the project version: Run "./gradlew updateVersion -Pfrom=OLD_VERSION" and try again.'
    }
}
