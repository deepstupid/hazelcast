/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.test;

import com.google.common.collect.Sets;
import org.reflections.Configuration;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.adapters.JavaReflectionAdapter;
import org.reflections.scanners.AbstractScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.net.URL;
import java.util.*;

/**
 * Initialize once org.reflections library to avoid duplicate work scanning the classpath from individual tests.
 */
public class ReflectionsHelper {

    public static final Reflections REFLECTIONS;

    static {
        // obtain all classpath URLs with com.hazelcast package classes
        Collection<URL> comHazelcastPackageURLs = ClasspathHelper.forPackage("com.hazelcast");
        // exclude hazelcast test artifacts from package URLs
        for (Iterator<URL> iterator = comHazelcastPackageURLs.iterator(); iterator.hasNext(); ) {
            URL url = iterator.next();
            // detect hazelcast-VERSION-tests.jar & $SOMEPATH/hazelcast/target/test-classes/ and exclude it from classpath
            // also exclude hazelcast-license-extractor artifact
            if (url.toString().contains("-tests.jar") || url.toString().contains("target/test-classes") ||
                    url.toString().contains("hazelcast-license-extractor")) {
                iterator.remove();
            }
        }
        HierarchyTraversingSubtypesScanner subtypesScanner = new HierarchyTraversingSubtypesScanner();
        subtypesScanner.setResultFilter(new FilterBuilder().exclude("java\\.lang\\.(Object|Enum)")
                                                           .exclude("com\\.hazelcast\\.com\\.eclipsesource.*"));
        REFLECTIONS = new ReflectionsTransitive(new ConfigurationBuilder().addUrls(comHazelcastPackageURLs)
                .addScanners(subtypesScanner, new TypeAnnotationsScanner())
                .setMetadataAdapter(new JavaReflectionAdapter()));
    }

    // Overrides default implementation of getSubTypesOf to obtain also transitive subtypes of given class
    public static class ReflectionsTransitive extends Reflections {

        public ReflectionsTransitive(Configuration configuration) {
            super(configuration);
        }

        @Override
        public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
            return Sets.newHashSet(ReflectionUtils.<T>forNames(
                    store.getAll(
                            HierarchyTraversingSubtypesScanner.class.getSimpleName(),
                            Arrays.asList(type.getName())),
                            configuration.getClassLoaders()));
        }
    }

    public static class HierarchyTraversingSubtypesScanner extends AbstractScanner{
        /**
         * creates new HierarchyTraversingSubtypesScanner. will exclude direct Object subtypes
         */
        public HierarchyTraversingSubtypesScanner() {
            this(true); //exclude direct Object subtypes by default
        }

        /**
         * creates new HierarchyTraversingSubtypesScanner.
         * @param excludeObjectClass if false, include direct {@link Object} subtypes in results.
         */
        public HierarchyTraversingSubtypesScanner(boolean excludeObjectClass) {
            if (excludeObjectClass) {
                filterResultsBy(new FilterBuilder().exclude(Object.class.getName())); //exclude direct Object subtypes
            }
        }

        // depending on Reflections configuration, cls is either a regular Class or a javassist ClassFile
        @SuppressWarnings({"unchecked"})
        public void scan(final Object cls) {
            String className = getMetadataAdapter().getClassName(cls);

            for (String anInterface : (List<String>) getMetadataAdapter().getInterfacesNames(cls)) {
                if (acceptResult(anInterface)) {
                    getStore().put(anInterface, className);
                }
            }

            // apart from this class' direct supertype and directly declared interfaces, also scan the class
            // hierarchy up until Object class
            Class superKlass = ((Class)cls).getSuperclass();
            while (superKlass != null) {
                scanClassAndInterfaces(superKlass, className);
                superKlass = superKlass.getSuperclass();
            }
        }

        private void scanClassAndInterfaces(Class klass, String className) {
            if (acceptResult(klass.getName())) {
                getStore().put(klass.getName(), className);
                for (String anInterface : (List<String>) getMetadataAdapter().getInterfacesNames(klass)) {
                    if (acceptResult(anInterface)) {
                        getStore().put(anInterface, className);
                    }
                }
            }
        }
    }
}
