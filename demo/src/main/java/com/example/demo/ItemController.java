package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    // Get all items (notes + files)
    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Add a text note
    @PostMapping("/note")
    public Item addNote(@RequestBody Item note) {
        return itemRepository.save(new Item(note.getContent()));
    }

    // Upload a file
    @PostMapping("/file")
    public Item uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Item item = new Item(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
        return itemRepository.save(item);
    }

    // Download a file
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        Item item = itemRepository.findById(id).orElseThrow();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(item.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + item.getFileName() + "\"")
                .body(item.getFileData());
    }

    // Delete an item (note or file)
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id) {
        itemRepository.deleteById(id);
    }
}
