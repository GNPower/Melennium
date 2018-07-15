package core.renderer;

import core.buffers.VAO;
import core.scene.Component;

public class Renderer extends Component{

	private VAO vao;
	private RenderInfo info;
	
	public Renderer() {}
	
	public void render() {
		info.getConfig().enable();
		info.getShader().bind();
		info.getShader().updateUniforms(getParent());
		vao.render(false);
		info.getConfig().disable();
	}
	
	public VAO getVao() {
		return vao;
	}
	public void setVAO(VAO vao) {
		this.vao = vao;
	}
	public RenderInfo getInfo() {
		return info;
	}
	public void setInfo(RenderInfo info) {
		this.info = info;
	}
}
