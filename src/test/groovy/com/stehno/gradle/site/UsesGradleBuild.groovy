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
package com.stehno.gradle.site

import groovy.text.GStringTemplateEngine
import groovy.text.TemplateEngine
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.rules.TemporaryFolder

import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by cjstehno on 3/17/17.
 */
trait UsesGradleBuild {

    // TODO: this is copied from one of my other plugins - add it to vanilla or something

    String getBuildTemplate() { '' }

    abstract TemporaryFolder getProjectRoot()

    private final TemplateEngine templateEngine = new GStringTemplateEngine()

    void buildFile(final Map<String, Object> config = [:]) {
        File buildFile = projectRoot.newFile('build.gradle')
        buildFile.text = (templateEngine.createTemplate(buildTemplate).make(config: config) as String).stripIndent()
    }

    // will not support lines with spaces in an argument even when quoted
    GradleRunner gradleRunner(final String line) {
        gradleRunner(line.split(' ') as List<String>)
    }

    GradleRunner gradleRunner(final List<String> args) {
        GradleRunner.create().withPluginClasspath().withDebug(true).withProjectDir(projectRoot.root).withArguments(args)
    }

    static boolean totalSuccess(final BuildResult result) {
        result.tasks.every { BuildTask task -> task.outcome != TaskOutcome.FAILED }
    }

    static boolean textContainsLines(final String text, final Collection<String> lines, final boolean trimmed = true) {
        lines.every { String line ->
            text.contains(trimmed ? line.trim() : line)
        }
    }

    static boolean textDoesNotContainLines(final String text, final Collection<String> lines, final boolean trimmed = true) {
        lines.every { String line ->
            !text.contains(trimmed ? line.trim() : line)
        }
    }

    // TODO: these should probably live somewhere else

    boolean fileOmitted(final String file) {
        !Files.exists(new File(projectRoot.root, file).toPath())
    }

    FileTreeBuilder fileTree() {
        new FileTreeBuilder(projectRoot.root)
    }

    boolean fileExists(final String file, final String expectedContent) {
        Path path = new File(projectRoot.root, file).toPath()
        assert Files.exists(path)
        assert path.text == expectedContent
        true
    }
}