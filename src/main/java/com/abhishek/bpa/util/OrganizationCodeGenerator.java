package com.abhishek.bpa.util;

import java.util.UUID;

public class OrganizationCodeGenerator {

    public static String generate(String organizationName){
        String slug = organizationName
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        String random = UUID.randomUUID()
                .toString()
                .substring(0, 4);

        return slug + "-" + random;
    }
}
