package com.educavalieri.dscatolog.services.validation;

import com.educavalieri.dscatolog.dto.UserInsertDTO;
import com.educavalieri.dscatolog.entities.User;
import com.educavalieri.dscatolog.repositories.UserRepository;
import com.educavalieri.dscatolog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        User user = userRepository.findByEmail(dto.getEmail());

        if (user != null ){
            list.add(new FieldMessage("email", "email already exists" ));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
