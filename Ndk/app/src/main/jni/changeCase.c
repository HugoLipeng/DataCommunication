#include <jni.h>
#include <stdio.h>

void changeCase(const char *fileName1, const char *fileName2) {
    FILE *fp1 = fopen(fileName1, "rt");
    FILE *fp2 = fopen(fileName2, "wt");

    char ch = (char) fgetc(fp1);

    while(!feof(fp1)) {
        if(ch >= 'a' && ch <= 'z') {
            ch -= 32;
        }
        fputc(ch, fp2);
        ch = (char) fgetc(fp1);
    }

    fclose(fp1);
    fclose(fp2);
}

JNIEXPORT void JNICALL
Java_imooc_android_com_changeCase_NdkUtils_convert(JNIEnv *env, jobject instance, jstring fileInput_,
                                                   jstring fileOutput_) {
    const char *fileInput = (*env)->GetStringUTFChars(env, fileInput_, 0);
    const char *fileOutput = (*env)->GetStringUTFChars(env, fileOutput_, 0);


    changeCase(fileInput, fileOutput);


    (*env)->ReleaseStringUTFChars(env, fileInput_, fileInput);
    (*env)->ReleaseStringUTFChars(env, fileOutput_, fileOutput);
}