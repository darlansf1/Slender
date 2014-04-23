#version 150

uniform mat4 u_modelMatrix;
uniform mat4 u_projectionMatrix;
uniform mat4 u_viewMatrix;

in vec3 a_position;
in vec3 a_normal;
in vec2 a_texcoord;

out vec3 v_normal;
out vec3 v_eye;
out vec2 v_texcoord;

void main(void)
{ 
  v_eye = -vec3(u_viewMatrix * u_modelMatrix * vec4(a_position, 1.0));

  //mat3 normal_mat = transpose(inverse(mat3(u_viewMatrix * u_modelMatrix)));
  mat3 normal_mat = mat3(u_viewMatrix * u_modelMatrix);
  v_normal = vec3(normal_mat * a_normal);

  v_texcoord = a_texcoord;

  gl_Position = u_projectionMatrix * u_viewMatrix * u_modelMatrix * vec4(a_position, 1.0);
}
  