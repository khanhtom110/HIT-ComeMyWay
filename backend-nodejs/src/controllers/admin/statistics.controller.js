import { sendResponse } from '../../utils/response.js';

export function createAdminStatisticsController(adminStatisticsModel) {
  return {
    async getStatistics(request, response) {
      return sendResponse(response, 200, await adminStatisticsModel.getStatistics());
    },
  };
}
