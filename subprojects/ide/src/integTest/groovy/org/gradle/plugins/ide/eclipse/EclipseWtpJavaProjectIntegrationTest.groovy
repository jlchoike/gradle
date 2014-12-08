/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.plugins.ide.eclipse

class EclipseWtpJavaProjectIntegrationTest extends AbstractEclipseIntegrationSpec {
    def "generates configuration files for a Java project"() {
        buildFile << """
apply plugin: 'eclipse-wtp'
apply plugin: 'java'

repositories {
    jcenter()
}

sourceCompatibility = 1.6

dependencies {
    compile 'com.google.guava:guava:18.0'
    testCompile "junit:junit:4.11"
}
"""

        when:
        run "eclipse"

        then:
        // Builders and natures
        def project = project
        project.assertHasJavaFacetNatures()
        project.assertHasJavaFacetBuilders()

        // Classpath
        def classpath = classpath
        classpath.assertHasLibs('guava-18.0.jar', 'junit-4.11.jar', 'hamcrest-core-1.3.jar')
        classpath.lib('guava-18.0.jar').assertIsMarkedForDeployment()
        classpath.lib('junit-4.11.jar').assertHasNoDeploymentAttributes()
        classpath.lib('hamcrest-core-1.3.jar').assertHasNoDeploymentAttributes()

        // Facets
        def facets = wtpFacets
        facets.assertHasFixedFacets("jst.java")
        facets.assertHasInstalledFacets("jst.utility", "jst.java")
        facets.assertFacetVersion("jst.utility", "1.0")
        facets.assertFacetVersion("jst.java", "6.0")

        // TODO - Deployment
    }
}