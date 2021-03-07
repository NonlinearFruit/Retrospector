namespace Retrospector.Core.Boundary
{
    public interface IUseCase<in T> where T : IRequest
    {
        void Execute(T request);
    }
}