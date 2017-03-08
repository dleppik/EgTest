#### Releasing a new version

0. Make sure everything is checked in and working!
1. Remove SNAPSHOT from version in top-level build.gradle
2. `./gradlew :egtest-annotations:uploadArchives`
3. Find the jars and pom and sign them with gpg2.
4. Follow the directions at http://central.sonatype.org/pages/manual-staging-bundle-creation-and-deployment.html
5. Change dependencies in egtest-processor to point at the repository
6. Repeat 2-4 for egtest-processor.
7. Create the new version on GitHub.
8. Roll back changes.
9. Update gist(s) and sample code to reflect new version numbers.
