package com.intellias.testmarketplace.model;

import com.intellias.testmarketplace.service.RoleService;

import java.beans.PropertyEditorSupport;
import java.util.UUID;

public class RoleEditor extends PropertyEditorSupport {
    private RoleService roleService;

    public RoleEditor(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text.equals("")) {
            this.setValue(null);
        } else {
            final String[] split = text.split(",");
            UUID id = UUID.fromString(split[0].trim());
            Role role = roleService.findById(id);
            this.setValue(role);
        }
    }

    @Override
    public String getAsText() {
        Role parent = new Role();
        if (this.getValue() != null) {
            parent = (Role) this.getValue();
            return parent.getId().toString();
        }
        return "";
    }
}
