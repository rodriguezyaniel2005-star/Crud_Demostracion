package org.example.controller;

import org.example.business.BusinessLogic;
import org.bson.types.ObjectId;

public class BaseController {
    protected BusinessLogic businessLogic;
    
    public BaseController() {
        this.businessLogic = new BusinessLogic();
    }
    
    protected boolean isValidObjectId(String id) {
        try {
            new ObjectId(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    protected ObjectId stringToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
