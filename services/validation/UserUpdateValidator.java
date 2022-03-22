package com.educavalieri.dscatolog.services.validation;

import com.educavalieri.dscatolog.dto.UserDTO;
import com.educavalieri.dscatolog.dto.UserInsertDTO;
import com.educavalieri.dscatolog.dto.UserUpdateDTO;
import com.educavalieri.dscatolog.entities.User;
import com.educavalieri.dscatolog.repositories.UserRepository;
import com.educavalieri.dscatolog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userID = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        User user = userRepository.findByEmail(dto.getEmail());

        if (user != null && userID != user.getId() ){
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
