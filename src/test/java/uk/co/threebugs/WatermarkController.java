package uk.co.threebugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class WatermarkController {

    private final WatermarkService watermarkService;

    @Autowired
    public WatermarkController(WatermarkService watermarkService) {
        this.watermarkService = watermarkService;
    }

    @RequestMapping(value = "/api/watermark", method = RequestMethod.POST)
    public ResponseEntity<?> watermark(@RequestBody Document document) {

        WatermarkResult watermarkResult = watermarkService.submit(document);

        if (watermarkResult.isAlreadySubmitted()) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(watermarkResult.getTicket());
    }

    @RequestMapping(value = "/api/watermark", method = RequestMethod.GET)
    public ResponseEntity<?> watermark(@RequestParam(value = "ticket") String ticket) {

        WatermarkedDocument watermarkedDocument = watermarkService.get(ticket);

        ResponseEntity<?> response;

        switch (watermarkedDocument.getStatus()) {

            case Complete:
                response = ResponseEntity.ok(watermarkedDocument.getDocument());
                break;
            case AlreadyProcessing:
                response = ResponseEntity.status(HttpStatus.ACCEPTED).build();
                break;
            case UnknownTicket:
                response = ResponseEntity.badRequest().build();
                break;
            default:
                throw new IllegalStateException("Unknown status " + watermarkedDocument.getStatus());
        }

        return response;
    }
}
