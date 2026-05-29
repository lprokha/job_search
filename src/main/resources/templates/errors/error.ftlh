<#import "../layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-7">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4 text-center">

                    <h2 class="mb-3">
                        <#if status?? && status == 403>
                            <@spring.message "error.forbiddenTitle"/>
                        <#else>
                            <@spring.message "error.title"/>
                        </#if>
                    </h2>

                    <p class="text-muted mb-4">
                        <#if status?? && status == 403>
                            <@spring.message "error.forbiddenMessage"/>
                        <#else>
                            <@spring.message "error.message"/>
                        </#if>
                    </p>

                    <div class="d-flex justify-content-center">
                        <a href="/profile" class="btn btn-dark btn-sm text-nowrap">
                            <@spring.message "button.backToProfile"/>
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</@main.layout>