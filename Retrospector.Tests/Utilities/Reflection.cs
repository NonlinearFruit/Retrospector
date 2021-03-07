namespace Retrospector.Tests.Utilities
{
    public static class Reflection
    {
        public static void SetProperty<T>(object obj, string property, T value)
        {
            obj.GetType().GetProperty(property).SetValue(obj, value);
        }

        public static T GetProperty<T>(object obj, string property)
        {
            return (T) obj.GetType().GetProperty(property).GetValue(obj);
        }
    }
}