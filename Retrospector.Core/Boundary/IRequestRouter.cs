namespace Retrospector.Core.Boundary
{
    public interface IRequestRouter
    {
        void Disseminate(IRequest request);
    }
}