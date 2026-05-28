<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">

                    <h2 class="mb-4 text-center">
                        <@spring.message "register.title"/>
                    </h2>

                    <form action="/register" method="post">

                        <#if _csrf??>
                            <input
                                    type="hidden"
                                    name="${(_csrf.parameterName)!'csrf'}"
                                    value="${(_csrf.token)!''}">
                        </#if>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.name"/>
                                </label>

                                <input type="text"
                                       name="name"
                                       class="form-control"
                                       value="${user.name!''}">

                                <@spring.bind "user.name"/>
                                <#if spring.status.error>
                                    <div class="text-danger small mt-1">
                                        <#list spring.status.errorMessages as error>
                                            <div>${error}</div>
                                        </#list>
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.surname"/>
                                </label>

                                <input type="text"
                                       name="surname"
                                       class="form-control"
                                       value="${user.surname!''}">

                                <@spring.bind "user.surname"/>
                                <#if spring.status.error>
                                    <div class="text-danger small mt-1">
                                        <#list spring.status.errorMessages as error>
                                            <div>${error}</div>
                                        </#list>
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "register.accountType"/>
                            </label>

                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="accountType"
                                       value="APPLICANT"
                                       <#if !user.accountType?? || user.accountType?string == "APPLICANT">checked</#if>>

                                <label class="form-check-label">
                                    <@spring.message "register.applicant"/>
                                </label>
                            </div>

                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="accountType"
                                       value="EMPLOYER"
                                       <#if user.accountType?? && user.accountType?string == "EMPLOYER">checked</#if>>

                                <label class="form-check-label">
                                    <@spring.message "register.employer"/>
                                </label>
                            </div>

                            <@spring.bind "user.accountType"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.email"/>
                                </label>

                                <input type="email"
                                       name="email"
                                       class="form-control"
                                       value="${user.email!''}">

                                <@spring.bind "user.email"/>
                                <#if spring.status.error>
                                    <div class="text-danger small mt-1">
                                        <#list spring.status.errorMessages as error>
                                            <div>${error}</div>
                                        </#list>
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.phoneNumber"/>
                                </label>

                                <input type="text"
                                       name="phoneNumber"
                                       class="form-control"
                                       value="${user.phoneNumber!''}">

                                <@spring.bind "user.phoneNumber"/>
                                <#if spring.status.error>
                                    <div class="text-danger small mt-1">
                                        <#list spring.status.errorMessages as error>
                                            <div>${error}</div>
                                        </#list>
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.age"/>
                                </label>

                                <input type="number"
                                       name="age"
                                       class="form-control"
                                       value="${user.age!''}">

                                <@spring.bind "user.age"/>
                                <#if spring.status.error>
                                    <div class="text-danger small mt-1">
                                        <#list spring.status.errorMessages as error>
                                            <div>${error}</div>
                                        </#list>
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.password"/>
                                </label>

                                <input type="password"
                                       name="password"
                                       class="form-control">

                                <@spring.bind "user.password"/>
                                <#if spring.status.error>
                                    <div class="text-danger small mt-1">
                                        <#list spring.status.errorMessages as error>
                                            <div>${error}</div>
                                        </#list>
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-dark">
                            <@spring.message "button.register"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>