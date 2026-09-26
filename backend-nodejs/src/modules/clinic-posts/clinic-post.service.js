import { validateClinicPost } from './clinic-post.validation.js';

export function createClinicPostService(repository) {
  return {
    create(clinicId, input) {
      const { title, content } = validateClinicPost(input);
      return repository.create(clinicId, title, content);
    },
    listByClinic(clinicId) {
      return repository.listByClinic(clinicId);
    },
    listPublic() {
      return repository.listPublic();
    },
  };
}
