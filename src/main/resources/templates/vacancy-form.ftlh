<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">

                    <h2 class="mb-4">
                        <#if vacancyId??>
                            <@spring.message "vacancy.editTitle"/>
                        <#else>
                            <@spring.message "vacancy.createTitle"/>
                        </#if>
                    </h2>

                    <form action="<#if vacancyId??>/my-vacancies/edit/${vacancyId}<#else>/my-vacancies/create</#if>"
                          method="post">

                        <#if _csrf??>
                            <input
                                    type="hidden"
                                    name="${(_csrf.parameterName)!'csrf'}"
                                    value="${(_csrf.token)!''}">
                        </#if>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.name"/>
                            </label>

                            <input type="text"
                                   name="name"
                                   class="form-control"
                                   value="${vacancy.name!''}">

                            <@spring.bind "vacancy.name"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.description"/>
                            </label>

                            <textarea name="description"
                                      class="form-control"
                                      rows="4">${vacancy.description!''}</textarea>

                            <@spring.bind "vacancy.description"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "label.category"/>
                            </label>

                            <select name="categoryId" class="form-select">
                                <option value="">
                                    <@spring.message "category.select"/>
                                </option>

                                <#list categories as category>
                                    <option value="${category.id}"
                                            <#if vacancy.categoryId?? && vacancy.categoryId == category.id>selected</#if>>
                                        ${category.name}
                                    </option>
                                </#list>
                            </select>

                            <@spring.bind "vacancy.categoryId"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.salary"/>
                            </label>

                            <input type="number"
                                   step="0.01"
                                   name="salary"
                                   class="form-control"
                                   value="<#if vacancy.salary??>${vacancy.salary?c}</#if>">

                            <@spring.bind "vacancy.salary"/>
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
                                    <@spring.message "vacancy.expFrom"/>
                                </label>

                                <input type="number"
                                       name="expFrom"
                                       class="form-control"
                                       value="${vacancy.expFrom!''}">

                                <@spring.bind "vacancy.expFrom"/>
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
                                    <@spring.message "vacancy.expTo"/>
                                </label>

                                <input type="number"
                                       name="expTo"
                                       class="form-control"
                                       value="${vacancy.expTo!''}">

                                <@spring.bind "vacancy.expTo"/>
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
                                <@spring.message "label.activity"/>
                            </label>

                            <select name="isActive" class="form-select">
                                <option value="true"
                                        <#if vacancy.isActive?? && vacancy.isActive>selected</#if>>
                                    <@spring.message "activity.active"/>
                                </option>

                                <option value="false"
                                        <#if vacancy.isActive?? && !vacancy.isActive>selected</#if>>
                                    <@spring.message "activity.inactive"/>
                                </option>
                            </select>

                            <@spring.bind "vacancy.isActive"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <button type="submit" class="btn btn-dark">
                            <#if vacancyId??>
                                <@spring.message "button.save"/>
                            <#else>
                                <@spring.message "button.create"/>
                            </#if>
                        </button>

                        <a href="/my-vacancies"
                           class="btn btn-outline-secondary ms-2">
                            <@spring.message "button.back"/>
                        </a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>