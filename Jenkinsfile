#!groovy

/**
 * PATH requirements: sbt, curl, jdk8  (configure in jenkins node)
 */
pipeline {
    agent {
        label 'linux-node'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '7')) // keep only recent builds
    }
    triggers {
        pollSCM('H/15 * * * *')
        //upstream(upstreamProjects: "<comma delimited list>", threshold: hudson.model.Result.SUCCESS)
    }

    stages {
        stage('Checkout sources') {
            steps {
                checkout scm
            }
        }

        stage ('build') {
            steps {
                echo "PATH is: $PATH"
                // this builds and tests
                cmd("sbt assembly")
            }
        }

        stage('documentation') {
            steps {
                cmd("sbt doc")
            }
        }

        stage('deploy') {
            steps {
                cmd("sbt publish")
            }
        }
    }

    post {
        always {
            junit "target/test-reports/*.xml"
            step([$class: 'JavadocArchiver', javadocDir: 'target/scala-2.11/api', keepAll: true])
        }
        success {
            //if (!"SUCCESS".equals(currentBuild.getPreviousBuild())) {
                echo 'This build was SUCCESSFUL!'
                mail to: 'bbe@esi-group.com',
                     subject: "Successful Pipeline: ${currentBuild.fullDisplayName}",
                     body: "This build succeeded: ${env.BUILD_URL}"
            //}
        }
        failure {
            echo 'This build FAILED!'
            mail to: 'bbe@esi-group.com',
                 subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                 body: """Something is wrong with ${env.BUILD_URL}.
                   It is failing in ${env.FAILURE_STAGE} stage.
                   \u2639 ${env.JOB_NAME} (${env.BUILD_NUMBER}) has failed.
                   """
        }
        unstable {
            echo 'This build is unstable.'
        }
    }
}

def cmd(cmd) {
    if (isUnix()) {
        sh "${cmd}"
    } else {
        bat "${cmd}"
    }
}