#version 150

struct LightProperties
{ 
    vec4 position;
    vec4 direction;
    float cosCutoff;
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
in vec3 eye;
in vec2 v_texcoord;
in vec4 v_position;

out vec4 fragColor;

void main(void)
{
    vec3 normal = normalize(v_normal);

    vec3 light_dir = vec3(u_light.direction);
    //vec3 light_dir = vec3((u_viewMatrix * u_modelMatrix * u_light.position) - v_position);

    vec4 Iamb = u_light.ambientColor * u_material.ambientColor;  

    float d = length(light_dir);
	float att = 0.0;
    light_dir = normalize(light_dir);

    if (dot(normal, light_dir) > u_light.cosCutoff){
		att = 1.0 / (u_light.constantAttenuation + 
	            (u_light.linearAttenuation*d) + 
	            (u_light.quadraticAttenuation*d*d));

	    vec4 Idiff = u_light.diffuseColor * u_material.diffuseColor;
	    float diff = max(dot(light_dir, normal), 0.0);
	    Idiff.rgb = Idiff.rgb * diff;

	    float spec = 0.0;

	    if(diff > 0.0) {
	      	vec3 reflection = normalize(reflect(-light_dir, normal));
	      	spec = pow(max(dot(eye, reflection), 0.0), u_material.specularExponent);

	      	if(u_is_texture) {
	            Idiff += texture(u_texture, v_texcoord) * diff;
	        } else {
	            Idiff += u_light.diffuseColor * u_material.diffuseColor * diff;
	        }
	    }
	    vec4 Ispec = u_light.specularColor * u_material.specularColor;
	    Ispec.rgb = Ispec.rgb * spec;
	    fragColor = clamp(Iamb + (att * (Idiff + Ispec)), 0.0, 1.0);
	}else{
		fragColor = clamp(Iamb, 0.0, 1.0);
	}
}