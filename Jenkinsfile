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
                echo env.GIT_LOCAL_BRANCH
            }
        }
        stage('Run pipeline against gradle') {
            when { 
                anyOf {
                    branch 'main'
                    branch 'feature'
                }
            }
            steps {
                echo env.GIT_BRANCH
                echo env.GIT_LOCAL_BRANCH
                container('gradle') {
                    sh 'pwd'
                    sh 'ls -la'
                    sh 'gradle wrapper'
                    sh 'chmod +x gradlew'
                    sh './gradlew test'
                }
            }
        }
        stage('CheckStyle Test') {
            when { 
                anyOf {
                    branch 'main'
                    branch 'feature'
                }
            }
            steps {
                echo env.GIT_BRANCH
                echo env.GIT_LOCAL_BRANCH
                container('gradle') {
                    sh './gradlew checkstyleMain'
                }
            }
            post {
                success {
                    // publish html
                    publishHTML (target: [
                        alwaysLinkToLastBuild: true,
                        reportDir: 'build/reports/checkstyle/',
                        reportFiles: 'main.html',
                        reportName: 'Checkstyle Report'
                    ])
                }
            }
        }
        stage('Code Coverage Test') {
            when {
                    branch 'main'
            }
            steps {
                container('gradle') {
                    sh 'pwd'
                    sh 'ls -la'
                    sh './gradlew jacocoTestCoverageVerification'
                    sh './gradlew jacocoTestReport'
                }
            }
            post {
                success {
                    // publish HTML
                    publishHTML (target: [
                        reportDir: 'build/reports/jacoco/test/html',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage Report'
                    ])
                }
           }
        }

/*      stage('Build a gradle project') {
            steps {
                container('gradle') {
                    sh '''
                    chmod +x gradlew
                    ./gradlew build
                    mv ./build/libs/calculator-0.0.1-SNAPSHOT.jar /mnt
                    '''
                }
            }
        }
        stage('Build Java Image') {
            steps {
                container('kaniko') {
                    sh '''
                    echo 'FROM openjdk:8-jre' > Dockerfile
                    echo 'COPY ./calculator-0.0.1-SNAPSHOT.jar app.jar' >> Dockerfile
                    echo 'ENTRYPOINT ["java", "-jar", "app.jar"]' >> Dockerfile
                    mv /mnt/calculator-0.0.1-SNAPSHOT.jar .
                    /kaniko/executor --context 'pwd' --destination mbellanfonte/hello-kaniko:1.0
                    '''
                }
            }
        }
*/    }
}
