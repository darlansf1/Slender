#version 150

uniform mat4 u_modelMatrix;
uniform mat4 u_projectionMatrix;
uniform mat4 u_viewMatrix;

in vec3 a_position;
in vec3 a_normal;
in vec2 a_texcoord;

out vec4 v_position;
out vec3 v_normal;
out vec3 v_eye;
out vec3 v_light;
out vec2 v_texcoord;

void main(void){
	vec4 v_position = u_viewMatrix * u_modelMatrix * vec4(a_position, 1.0);

	v_normal = mat3(u_viewMatrix * u_modelMatrix) * a_normal;

	v_eye = vec3(-v_position);

	v_light = vec3(0.0, -0.2, 0.2)-vec3(v_position);

	v_texcoord = a_texcoord;

	gl_Position = u_projectionMatrix * v_position;
}