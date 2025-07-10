package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import lombok.Getter;

@Getter
public enum AuthErrorType {
    Student_Syntax("""
            輸入的學號格式錯誤。
            > Wrong student id syntax.
            """),
    Phone_Syntax("""
            輸入的電話號碼格式錯誤。
            > Wrong phone number syntax.
            """),
    Mail_Syntax("""
            輸入的電子郵件格式錯誤。
            > Wrong email syntax.
            """),
    Empty("""
            請輸入學號及電話號碼或電子郵件。
            > Can not enter empty value
            """),
    Linked("""
            您此帳號已驗證過住宿生身份，請勿重複嘗試。
            > Your account has already been verified as a dormitory resident. Please do not try again.
            """),
    Not_Student("""
            查無此人。
            > Have no this student
            """),
    Linked_By_Other("""
            您所輸入的住宿生身份已被他人所綁定。
            > The dormitory resident identity you entered has already been link to another account.
            """),
    Not_Current_living("""
            無法將您的資料對應到本學期之床位，您可能非本學期住宿生或已離宿，若有任何問題請聯繫宿舍幹部。
            > You are not a dormitory resident for the current semester, so verification is not possible.
            """),
    NONE("""
            恭喜！您已通過驗證。若有任何問題請聯繫宿舍幹部。
            > Congratulations! You have successfully passed the verification. If you have any questions, please contact the dormitory staff.
            """)
    ;
    final String msg;

    AuthErrorType(String msg) {
        this.msg = msg;
    }
}
