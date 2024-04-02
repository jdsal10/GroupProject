plugins {
    id("com.android.application")
}

android {
    namespace = "com.firstapp.group10app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.firstapp.group10app"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.sun.mail:android-mail:1.6.6")
    implementation("com.sun.mail:android-activation:1.6.6")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.api-client:google-api-client:2.2.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")
    implementation("com.google.prefab:api:2.1.0")
    implementation("com.google:google:5")
    implementation("net.sourceforge.jtds:jtds:1.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.api-client:google-api-client:2.2.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")
    implementation("com.google.http-client:google-http-client-gson:1.43.3")
    implementation("mysql:mysql-connector-java:5.1.49")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.fragment:fragment:1.6.2")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    implementation("androidx.activity:activity-compose")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
}

android {
    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("(ETA-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/*.kotlin_module")
        resources.excludes.add("META-INF/NOTICE.md")
    }
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "33.0.1"
}