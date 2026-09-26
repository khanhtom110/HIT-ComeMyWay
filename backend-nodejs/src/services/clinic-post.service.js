export function createClinicPostService(clinicPostModel) {
  return {
    create(clinicId, { title, content }) {
      return clinicPostModel.create(clinicId, title, content);
    },
    listByClinic(clinicId) {
      return clinicPostModel.listByClinic(clinicId);
    },
    listPublic() {
      return clinicPostModel.listPublic();
    },
  };
}
