import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestType {

    public List<String> getListString(){
        return new ArrayList<String>();
    }

    public List<Map<String,String>> getListMapString(){
        return new ArrayList<Map<String,String>>();
    }

    public <T> List<byte[]> getListArrayString(){
        return new ArrayList<byte[]>();
    }

    public static void main(String[] args) {
        Class clazz = TestType.class;
        for (Method method:clazz.getDeclaredMethods()){
            System.out.println(method.getName());
            Type returnType = method.getGenericReturnType();
            System.out.println(returnType);
            if(returnType instanceof ParameterizedType){
                Type[] actualTypeArguments = ((ParameterizedType)returnType).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    Type returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        // (issue #443) actual type can be a also a parameterized type
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    } else if (returnTypeParameter instanceof GenericArrayType) {
                        Type temp = ((GenericArrayType) returnTypeParameter).getGenericComponentType();
                        System.out.println(temp);
                        Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter).getGenericComponentType();
                        // (issue #525) support List<byte[]>
                        returnType = Array.newInstance(componentType, 0).getClass();
                    }
                    System.out.println(returnType);

                    System.out.println(Map.class.isAssignableFrom((Class<?>) returnType));
                }
            }

            System.out.println(Object.class.getName());

        }
    }
}
