package com.abhishek.bpa.util;

import java.util.UUID;

public class OrganizationCodeGenerator {

    public static String toSlug(String organizationName){
        return organizationName
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
