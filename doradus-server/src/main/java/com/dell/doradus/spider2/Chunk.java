package com.dell.doradus.spider2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//list of objects sorted by id and stored in Cassandra with lz4 compression
//all objects ids are greader or equal than chunkId.
//initially, there is only one chunk with chunkId = ""
//if the chunk gets too big, it is split in two, first chunk gets id of
//the parent chunk and its first half of objects and second chunk
//gets chunkId of the median object and last half of objects
public class Chunk {
    private Binary m_chunkId;
    private List<S2Object> m_objects = new ArrayList<>();

    public Chunk(Binary chunkId) { m_chunkId = chunkId; }

    public Chunk(Binary chunkId, List<S2Object> objects) { m_chunkId = chunkId; m_objects = objects; }
    
    public Binary getChunkId() { return m_chunkId; }
    
    public int size() { return m_objects.size(); }
    
    public Collection<S2Object> getObjects() { return m_objects; }
    
    public void add(S2Object obj) {
        m_objects.add(obj);
    }
    
    public byte[] toByteArray() {
        MemoryStream buffer = new MemoryStream();
        for(S2Object obj: m_objects) {
            obj.write(buffer);
        }
        byte[] data = buffer.toArray();
        data = ChunkCompression.compress(data);
        return data;
    }
    
    public static Chunk fromByteArray(Binary chunkId, byte[] data) {
        data = ChunkCompression.decompress(data);
        Chunk chunk = new Chunk(chunkId);
        MemoryStream buffer = new MemoryStream(data);
        while(!buffer.end()) {
            S2Object obj = S2Object.read(buffer);
            chunk.add(obj);
        }
        return chunk;
    }
    
    //actually, we can split in more than two chunks, we need all chunks to be smaller than
    //maxSize. First chunk always gets parent chunk's id
    public List<Chunk> split(int maxSize) {
        int size = size();
        if(size <= maxSize) throw new RuntimeException("Chunk is too small to split");
        int splitParts = (size + maxSize - 1) / maxSize;
        int splitSize = (size + splitParts - 1) / splitParts;
        List<Chunk> chunks = new ArrayList<Chunk>(splitParts);
        //first chunk gets the id of the parent chunk
        Chunk chunk = new Chunk(getChunkId());
        chunks.add(chunk);
        for(S2Object obj: getObjects()) {
            if(chunk.size() >= splitSize) {
                chunk = new Chunk(obj.getId());
                chunks.add(chunk);
            }
            chunk.add(obj);
        }
        return chunks;
    }
}
