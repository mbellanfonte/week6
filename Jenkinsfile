// Uses Declarative syntax to run commands inside a container.
pipeline {
    agent {
        kubernetes {
            // Rather than inline YAML, in a multibranch Pipeline you could use: yamlFile 'jenkins-pod.yaml'
            // Or, to avoid YAML:
            // containerTemplate {
            //     name 'shell'
            //     image 'ubuntu'
            //     command 'sleep'
            //     args 'infinity'
            // }
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:6.3-jdk14
    command:
    - sleep
    args:
    - 30d
'''
        }
    }
    stages {
        stage('Debug') {
            steps {
                echo env.GIT_BRANCH
                echo env. GIT_LOCAL_BRANCH
            }
        }
        stage('Run pipeline against gradle') {
            steps {
                git 'https://github.com/mbellanfonte/week6.git'
                container('gradle') {
                    sh '''
                    chmod +x gradlew
                    ./gradlew test
                    '''
                }
            }
        }
        stage('Main - JaCoCo Test Coverage') {
            when {
                //beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/main"
                }
            }
            steps {
                sh '''
                echo "I am the main branch"
                ./gradlew jacocoTestCoverageVerification
                ./gradlew jacocoTestReport
                '''
                publishHTML (target: [
                    reportDir: 'week6/build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage Report'
                ])
            }
        }
        stage('Main - Checkstyle Test') {
            when {
                //beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/main"
                }
            }
            steps {
                script {
                    try {
                        sh '''
                        pwd
                        cd week6
                        ./gradlew checkstyleMain'''
                    } catch (Exception e) {
                        echo 'checkstyle fails'
                    }
                    publishHTML (target: [
                        alwaysLinkToLastBuild: true,
                        reportDir: 'week6/build/reports/checkstyle/',
                        reportFiles: 'main.html',
                        reportName: 'Main Checkstyle Report'
                    ])
                }
            }
        }

        stage('Feature') {
            when {
                //beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/feature"
                }
            }
            steps {
                script {
                    echo "I am a feature branch"
                    try {
                        sh '''
                        pwd
                        cd week6
                        ./gradlew checkstyleMain'''
                    } catch (Exception e) {
                        echo 'checkstyle fails'
                    }
                    publishHTML (target: [
                        alwaysLinkToLastBuild: true,
                        reportDir: 'week6/build/reports/checkstyle/',
                        reportFiles: 'main.html',
                        reportName: 'Feature Checkstyle Report'
                    ])
                }
            }
        }
        stage('Playground') {
            when {
                //beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/playground"
                }
            }
            steps {
                echo "I am a playground branch"
            }
        }
    }
}
