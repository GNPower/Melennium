package core.modules.terrain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import core.model.Material;
import core.modules.gpgpu.NormalMapRenderer;
import core.modules.gpgpu.SplatMapRenderer;
import core.texturing.Texture2D;
import core.utils.BufferUtil;
import core.utils.Util;

public class TerrainConfig {

	private float scaleY, scaleXZ;
	
	private Texture2D heightmap, normalMap, splatmap;
	
	private FloatBuffer heightmapDataBuffer;
	
	private int tessellationFactor;
	private float tessellationSlope;
	private float tessellationShift;
	private int tbn_range;
	
	private int[] lod_range = new int[8];
	private int[] lod_morphing_area = new int[8];
	
	private List<Material> materials = new ArrayList<>();
	
	public void loadFile(String file) {
		BufferedReader reader =  null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0)
					continue;
				
				if(tokens[0].equals("scaleY")) {
					setScaleY(Float.valueOf(tokens[1]));
				}
				if(tokens[0].equals("scaleXZ")) {
					setScaleXZ(Float.valueOf(tokens[1]));
				}
				if(tokens[0].equals("heightmap")) {
					heightmap = new Texture2D(tokens[1]);
					getHeightMap().bind();
					getHeightMap().bilinearFilter();
					
					createHeightmapDataBuffer();
					
					NormalMapRenderer normalMapRenderer = new NormalMapRenderer(getHeightMap().getWidth());
					normalMapRenderer.setStrength(60);
					normalMapRenderer.render(getHeightMap());
					setNormalMap(normalMapRenderer.getNormalMap());
					
					
					SplatMapRenderer splatMapRenderer = new SplatMapRenderer(getHeightMap().getWidth());
					splatMapRenderer.render(getNormalMap());
					setSplatmap(splatMapRenderer.getSplatmap());
				}
				if(tokens[0].equals("tessellationFactor")) {
					setTessellationFactor(Integer.valueOf(tokens[1]));
				}
				if(tokens[0].equals("tessellationSlope")) {
					setTessellationSlope(Float.valueOf(tokens[1]));
				}
				if(tokens[0].equals("tessellatioonShift")) {
					setTessellationShift(Float.valueOf(tokens[1]));
				}
				
				if(tokens[0].equals("#lod_ranges")) {
					for(int i = 0; i < lod_range.length; i++) {
						line = reader.readLine();
						tokens = line.split(" ");
						tokens = Util.removeEmptyStrings(tokens);
						
						if(tokens[0].equals("lod" + (i + 1) + "_range")) {
							if(Integer.valueOf(tokens[1]) == 0) {
								lod_range[i] = 0;
								lod_morphing_area[i] = 0;
							}else {
								setLodRange(i, Integer.valueOf(tokens[1]));
							}
						}
					}
				}
				if(tokens[0].equals("tbn_range")) {
					setTbn_range(Integer.valueOf(tokens[1]));
				}
				if(tokens[0].equals("material")) {
					getMaterials().add(new Material());
				}
				if(tokens[0].equals("material_DIF")) {
					Texture2D diffusemap = new Texture2D(tokens[1]);
					diffusemap.bind();
					diffusemap.trilinearFilter();
					getMaterials().get(materials.size()-1).setDiffusemap(diffusemap);
				}
				if(tokens[0].equals("material_NRM")) {
					Texture2D normalmap = new Texture2D(tokens[1]);
					normalmap.bind();
					normalmap.trilinearFilter();
					getMaterials().get(materials.size()-1).setNormalmap(normalmap);
				}
				if(tokens[0].equals("material_DISP")) {
					Texture2D heightmap = new Texture2D(tokens[1]);
					heightmap.bind();
					heightmap.trilinearFilter();
					getMaterials().get(materials.size()-1).setDisplacemap(heightmap);
				}
				if(tokens[0].equals("material_heightScaling")) {
					materials.get(materials.size()-1).setDisplaceScale(Float.valueOf(tokens[1]));
				}
				if(tokens[0].equals("material_horizontalScaling")) {
					materials.get(materials.size()-1).setHorizontalScale(Float.valueOf(tokens[1]));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int updateMorphingArea(int lod) {
		return (int) ((scaleXZ / TerrainQuadtree.getRootNodes()) / (Math.pow(2, lod)));
	}
	
	private void setLodRange(int index, int lod_range) {
		this.lod_range[index] = lod_range;
		lod_morphing_area[index] = lod_range - updateMorphingArea(index + 1);
	}
	
	public void createHeightmapDataBuffer() {
		System.out.println("Created Buffer: " + getHeightMap().getWidth() * getHeightMap().getHeight());
		heightmapDataBuffer = BufferUtil.createFloatBuffer(getHeightMap().getWidth() * getHeightMap().getHeight());
		heightmap.bind();
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RED, GL11.GL_FLOAT, heightmapDataBuffer);
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	public float getScaleXZ() {
		return scaleXZ;
	}
	
	public void setScaleXZ(float scaleXZ) {
		this.scaleXZ = scaleXZ;
	}
	
	public int[] getLod_range() {
		return lod_range;
	}
	
	public void setLod_range(int[] lod_range) {
		this.lod_range = lod_range;
	}
	
	public int[] getLod_morphing_area() {
		return lod_morphing_area;
	}
	
	public void setLod_morphing_area(int[] lod_morphing_area) {
		this.lod_morphing_area = lod_morphing_area;
	}

	public int getTessellationFactor() {
		return tessellationFactor;
	}

	public void setTessellationFactor(int tessellationFactor) {
		this.tessellationFactor = tessellationFactor;
	}

	public float getTessellationSlope() {
		return tessellationSlope;
	}

	public void setTessellationSlope(float tessellationSlope) {
		this.tessellationSlope = tessellationSlope;
	}

	public float getTessellationShift() {
		return tessellationShift;
	}

	public void setTessellationShift(float tessellationShift) {
		this.tessellationShift = tessellationShift;
	}

	public Texture2D getHeightMap() {
		return heightmap;
	}

	public void setHeightMap(Texture2D heightMap) {
		this.heightmap = heightMap;
	}

	public Texture2D getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(Texture2D normalMap) {
		this.normalMap = normalMap;
	}

	public Texture2D getSplatmap() {
		return splatmap;
	}

	public void setSplatmap(Texture2D splatmap) {
		this.splatmap = splatmap;
	}

	public int getTbn_range() {
		return tbn_range;
	}

	public void setTbn_range(int tbn_range) {
		this.tbn_range = tbn_range;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}

	public FloatBuffer getHeightmapDataBuffer() {
		return heightmapDataBuffer;
	}

	public void setHeightmapDataBuffer(FloatBuffer heightmapDataBuffer) {
		this.heightmapDataBuffer = heightmapDataBuffer;
	}
}
