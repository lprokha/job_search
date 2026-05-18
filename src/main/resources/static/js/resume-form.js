window.addEventListener('load', function () {
    const resumeForm = document.getElementById('resume-form');
    const educationList = document.getElementById('education-list');
    const workList = document.getElementById('work-list');
    const addEducationButton = document.getElementById('add-education');
    const addWorkButton = document.getElementById('add-work');

    let educationIndex = 1;
    let workIndex = 1;

    addEducationButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'education-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationInstitution}</label>
                <input type="text" name="educationInfos[${educationIndex}].institution" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationProgram}</label>
                <input type="text" name="educationInfos[${educationIndex}].program" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationStartDate}</label>
                <input type="date" name="educationInfos[${educationIndex}].startDate" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationEndDate}</label>
                <input type="date" name="educationInfos[${educationIndex}].endDate" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationDegree}</label>
                <input type="text" name="educationInfos[${educationIndex}].degree" class="form-control">
            </div>
        `;

        educationList.appendChild(block);
        educationIndex++;
    });

    addWorkButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'work-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workYears}</label>
                <input type="number" name="workExperienceInfos[${workIndex}].years" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workCompany}</label>
                <input type="text" name="workExperienceInfos[${workIndex}].companyName" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workPosition}</label>
                <input type="text" name="workExperienceInfos[${workIndex}].position" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workResponsibilities}</label>
                <textarea name="workExperienceInfos[${workIndex}].responsibilities" class="form-control" rows="4"></textarea>
            </div>
        `;

        workList.appendChild(block);
        workIndex++;
    });

    resumeForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const formData = new FormData(resumeForm);
        const data = new URLSearchParams(formData);

        try {
            const response = await fetch(resumeForm.action, {
                method: 'POST',
                body: data
            });

            if (response.ok) {
                window.location.href = '/resumes';
            } else {
                alert(resumeForm.dataset.saveError);
            }
        } catch (error) {
            console.log(error);
            alert(resumeForm.dataset.requestError);
        }
    });
});