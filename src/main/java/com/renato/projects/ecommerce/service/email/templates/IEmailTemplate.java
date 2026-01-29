package com.renato.projects.ecommerce.service.email.templates;

import com.renato.projects.ecommerce.service.email.EmailData;

public interface IEmailTemplate<T> {
    EmailData build(T context);
}
