<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h2 class="mb-3">Смена пароля</h2>

                    <#if error??>
                        <div class="alert alert-danger">${error}</div>
                    <#else>
                        <p class="text-muted">
                            Введите новый пароль для вашего аккаунта.
                        </p>

                        <form action="/reset-password" method="post">
                            <#if _csrf??>
                                <input type="hidden"
                                       name="${(_csrf.parameterName)!'csrf'}"
                                       value="${(_csrf.token)!''}">
                            </#if>

                            <input type="hidden" name="token" value="${token!}">

                            <div class="mb-3">
                                <label for="password" class="form-label">Новый пароль</label>
                                <input type="password"
                                       id="password"
                                       name="password"
                                       class="form-control"
                                       placeholder="Введите новый пароль"
                                       required
                                       autofocus>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-dark">Сменить пароль</button>
                                <a href="/login" class="btn btn-outline-dark">Назад ко входу</a>
                            </div>
                        </form>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</@main.layout>