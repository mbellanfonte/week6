// Uses Declarative syntax to run commands inside a container.
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
    - 30d
'''
        }
    }
    stages {
        stage('Run pipeline against gradle') {
            steps {
                git 'https://github.com/mbellanfonte/week6.git'
                container('gradle') {
                    sh '''
		    pwd
                    chmod +x gradlew
                    ./gradlew test
                    '''
                    stage('Debug') {
                        steps {
                        echo env.GIT_BRANCH
                        echo env. GIT_LOCAL_BRANCH
                        }
                }
            }
        }
	}
    }
}
