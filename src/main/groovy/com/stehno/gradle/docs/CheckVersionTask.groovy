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
 * The "checkVersion" used to check if the expected files contain the correct version information (which is configured in the build.gradle file
 * version property). This check is done fairly loosely as a simple check of whether or not the expected version string appears in the file content.
 */
class CheckVersionTask extends DefaultTask {

    static final String NAME = 'checkVersion'

    @TaskAction @SuppressWarnings('GroovyUnusedDeclaration') void checkVersion() {
        logger.lifecycle "Verifying that the documentation references version ${project.version}..."

        SiteExtension extension = project.extensions.findByType(SiteExtension)

        // Not the most efficient way to do this but should be ok for now
        boolean documented = (['README.md'] + extension.versionedFiles).every { f ->
            File file = project.file(f)
            boolean ok = !file.exists() || file.text.contains(project.version)
            logger.info " - Checking: $f: $ok"
            ok
        }

        assert documented, 'The documented project version does not match the project version: Run "updateVersion -Pfrom=<old-version>" and try again.'
    }
}
