#version 150

struct LightProperties
{ 
    vec4 position;
    vec4 direction;
    vec4 ambientColor;
    vec4 diffuseColor;
    vec4 specularColor;
    float constantAttenuation;
    float linearAttenuation;
    float quadraticAttenuation;
};

struct MaterialProperties
{
    vec4 ambientColor;
    vec4 diffuseColor;
    vec4 specularColor;
    float specularExponent;
};

uniform	LightProperties u_light;
uniform	MaterialProperties u_material;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;

uniform sampler2D u_texture;
uniform bool u_is_texture;

in vec3 v_normal;
in vec4 v_position;
in vec2 v_texcoord;
in vec3 eye;

out vec4 fragColor;

void main(void)
{
    vec3 normal = normalize(v_normal);

    vec3 light_dir = vec3(u_light.direction);

    float d = length(light_dir);
	float att = 1.0 / (u_light.constantAttenuation + 
	            (u_light.linearAttenuation*d) + 
	            (u_light.quadraticAttenuation*d*d));

    light_dir = normalize(light_dir);

    vec4 Iamb = u_light.ambientColor * u_material.ambientColor;

    float diff = max(dot(light_dir, normal), 0.0);
    vec4 Idiff = u_light.diffuseColor * u_material.diffuseColor;
    

    float spec = 0.0;
    if(diff > 0.0) {
    	if(u_is_texture) {
            Idiff = texture(u_texture, v_texcoord) * diff;
        } else {
        	Idiff = Idiff * diff;
        }

      vec3 reflection = normalize(reflect(-light_dir, normal));
      spec = pow(max(dot(eye, reflection), 0.0), u_material.specularExponent);
    }
    vec4 Ispec = u_light.specularColor * u_material.specularColor;
    Ispec.rgb = Ispec.rgb * spec;

    if (att < 0.5)
    	fragColor = clamp(Iamb + (att * (Idiff + Ispec)), 0.0, 1.0);
    else
    	fragColor = vec4(0.0, 0.0, 0.0, 0.0);
}
