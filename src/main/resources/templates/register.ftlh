<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="mb-4 text-center">Регистрация пользователя</h2>

                    <form>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Имя</label>
                                <input type="text" class="form-control" placeholder="Введите имя">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Фамилия</label>
                                <input type="text" class="form-control" placeholder="Введите фамилию">
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Тип аккаунта</label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="accountType" checked>
                                <label class="form-check-label">Соискатель</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="accountType">
                                <label class="form-check-label">Работодатель</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" placeholder="Введите email">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Номер телефона</label>
                                <input type="text" class="form-control" placeholder="Введите номер телефона">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Возраст</label>
                                <input type="number" class="form-control" placeholder="Введите возраст">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Пароль</label>
                                <input type="password" class="form-control" placeholder="Введите пароль">
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Аватар</label>
                            <input type="file" class="form-control">
                        </div>

                        <button type="submit" class="btn btn-dark">Зарегистрироваться</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>