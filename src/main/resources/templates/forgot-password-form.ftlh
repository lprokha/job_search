<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h2 class="mb-3">Восстановление пароля</h2>

                    <p class="text-muted">
                        Введите email, указанный при регистрации. Мы отправим на него ссылку для смены пароля.
                    </p>

                    <#if error??>
                        <div class="alert alert-danger">${error}</div>
                    </#if>

                    <#if message??>
                        <div class="alert alert-success">${message}</div>
                    </#if>

                    <form action="/forgot-password" method="post">
                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!''}">
                        </#if>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email"
                                   id="email"
                                   name="email"
                                   class="form-control"
                                   placeholder="Введите email"
                                   required
                                   autofocus>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-dark">Отправить ссылку</button>
                            <a href="/login" class="btn btn-outline-dark">Назад ко входу</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>