// Uses Declarative syntax to run commands inside a container - remove /r.
pipeline {
    agent {
        kubernetes {
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
    - 99d
    volumeMounts:
    - name: shared-storage
      mountPath: /mnt
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command:
    - sleep
    args:
    - 9999999
    volumeMounts:
    - name: shared-storage
      mountPath: /mnt
    - name: kaniko-secret
      mountPath: /kaniko/.docker
  restartPolicy: Never
  volumes:
  - name: shared-storage
    persistentVolumeClaim:
      claimName: jenkins-pv-claim
  - name: kaniko-secret
    secret:
      secretName: dockercred
      items:
      - key: .dockerconfigjson
        path: config.json
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
//                git 'https://github.com/mbellanfonte/week6.git'
                container('gradle') {
                    sh '''
                    bash
                    gradle wrapper
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
                    return env.GIT_BRANCH == "main"
                }
            }
            steps {
                echo "MAIN BRANCH JaCoCo Test..."                 
                sh '''
                pwd
                cd /home/gradle
                gradle wrapper
                chmod +x gradlew
                .gradlew test
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
                    return env.GIT_BRANCH == "main"
                }
            }
            steps {
                echo "MAIN BRANCH Checkstyle test"
/*                 script {
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
 */          }
        }

        stage('Feature') {
            when {
                //beforeAgent true
                expression {
                    return env.GIT_BRANCH == "feature"
                }
            }
            steps {
                echo "FEATURE BRANCH Checkstyle Test"
/*                 script {
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
 */            }
        }
        stage('Playground') {
            when {
                //beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/playground"
                }
            }
            steps {
                echo "PLAYGROUND BRANCH -- No testing to be performed."
            }
        }
    }
}
