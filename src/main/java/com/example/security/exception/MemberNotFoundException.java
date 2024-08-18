package com.example.security.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String memberId) {
        super(memberId + " NotFoundException");
    }
}
