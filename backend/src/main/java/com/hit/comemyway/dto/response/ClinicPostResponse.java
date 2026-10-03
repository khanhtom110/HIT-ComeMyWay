package com.hit.comemyway.dto.response;

import java.util.List;

public record ClinicPostResponse(Long id,Long clinicId,String clinicName,String clinicAvatarUrl,String title,String content,String imageUrl,List<String>imageUrls){}
